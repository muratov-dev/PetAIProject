package me.yeahapps.mypetai.feature.profile.ui.viewmodel

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.yeahapps.mypetai.core.ui.viewmodel.BaseViewModel
import me.yeahapps.mypetai.feature.profile.domain.repository.MyWorksRepository
import me.yeahapps.mypetai.feature.profile.ui.action.MyWorksInfoAction
import me.yeahapps.mypetai.feature.profile.ui.event.MyWorksInfoEvent
import me.yeahapps.mypetai.feature.profile.ui.screen.MyWorksInfoScreen
import me.yeahapps.mypetai.feature.profile.ui.state.MyWorksInfoState
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyWorksInfoViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
    private val myWorksRepository: MyWorksRepository
) : BaseViewModel<MyWorksInfoState, MyWorksInfoEvent, MyWorksInfoAction>(MyWorksInfoState()) {

    val args = savedStateHandle.toRoute<MyWorksInfoScreen>()

    override fun obtainEvent(viewEvent: MyWorksInfoEvent) {
        when (viewEvent) {
            MyWorksInfoEvent.NavigateUp -> sendAction(MyWorksInfoAction.NavigateUp)
            MyWorksInfoEvent.DeleteWork -> {
                viewModelScoped {
                    myWorksRepository.deleteMyWork(args.workId)
                    sendAction(MyWorksInfoAction.NavigateUp)
                }
            }

            MyWorksInfoEvent.SaveToGallery -> {
                viewModelScoped {
                    currentState.workInfo?.let { saveVideoToGallery(it.videoPath, it.title) }
                }
            }
        }
    }

    init {
        viewModelScoped {
            val work = myWorksRepository.getMyWorkInfo(args.workId)
            updateViewState { copy(workInfo = work) }
        }
    }

    suspend fun saveVideoToGallery(path: String, displayName: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val sourceFile = File(path)
                if (!sourceFile.exists()) {
                    Timber.e("Файл не найден по пути: $path")
                    return@withContext false
                }

                val resolver = context.contentResolver
                val videoCollection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

                val values = ContentValues().apply {
                    put(MediaStore.Video.Media.DISPLAY_NAME, displayName)
                    put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
                    put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
                    put(MediaStore.Video.Media.IS_PENDING, 1)
                }

                val uri = resolver.insert(videoCollection, values) ?: return@withContext false

                resolver.openOutputStream(uri)?.use { outputStream ->
                    sourceFile.inputStream().use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }

                values.clear()
                values.put(MediaStore.Video.Media.IS_PENDING, 0)
                resolver.update(uri, values, null, null)

                true
            } catch (e: Exception) {
                Timber.e(e, "Ошибка при сохранении видео в галерею")
                false
            }
        }
    }
}