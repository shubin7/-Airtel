package com.example.android.airteldemo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.airteldemo.models.KeyAction
import com.example.android.airteldemo.network.ServerInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class MainVM : ViewModel() {

    // Live data to store the response from the server
    private val ldKeyActions = MutableLiveData<ArrayList<KeyAction>>()
    // Parent job for effective cancellation of coroutines
    private var job: Job? = null

    fun getKeyActionsLd(api: ServerInterface = ServerInterface.getApi()): LiveData<ArrayList<KeyAction>> {

        // Cancel any previous jobs
        job?.cancel()

        // Launch the job on the Default dispatcher which should take the processing off Main thread
        job = GlobalScope.launch(Dispatchers.Default) {
            try {
                // Await and get the body of the response
                val responseBody = api.getAllCheckpointResponse().await().body()
                Timber.d("getKeyActionsLd: response: $responseBody")

                // If success, pass on the response to the LiveData observers
                if (responseBody != null && responseBody.status == 200) {

                    val infoList = responseBody.info
                    Timber.d("getKeyActionsLd: infoList: $infoList")

                    // post the values
                    ldKeyActions.postValue(infoList)
                } else {
                    // todo Event to be passed back to calling Activity or through event buses.
                    Timber.e("getKeyActionsLd: Error occurred: ${responseBody?.info}")
                }

            } catch (e: Exception) {
                // todo handle error case| Could be handled in multiple ways. Usually done through a repository
                e.printStackTrace()
            }
        }

        return ldKeyActions
    }

    override fun onCleared() {
        super.onCleared()

        // Cancel the job when the VM is cleared
        job?.cancel()
    }
}