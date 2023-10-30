package com.example.taipeitravel.ui.main.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taipeitravel.models.Attraction
import com.example.taipeitravel.models.TravelPageResponse
import com.example.taipeitravel.repository.TravelRepository
import com.example.taipeitravel.utilities.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: TravelRepository) : ViewModel() {

    private val _attractions = MutableLiveData(ArrayList<Attraction>())
    val attractions: LiveData<ArrayList<Attraction>> = _attractions

    private var pageIdx = 0

    // fetch attractions data
    fun fetch(reload: Boolean = false, callback: (() -> Unit)? = null) {
        if (reload) {
            pageIdx = 0
        }
        GlobalScope.launch(Dispatchers.IO) {
            var res: TravelPageResponse<Attraction>? = null
            try {
                res = repo.getAttractions(Utils.getLangForWebapi(), pageIdx + 1)
            } catch (e: Exception) {
                Log.d("tag", e.stackTraceToString())
                Utils.toast("failed to fetch data!")
            }
            runBlocking {
                delay(1000)
            }
            if (reload) {
                _attractions.value!!.clear()
            }
            if (res != null && res.data.isNotEmpty()) {
                _attractions.value!!.addAll(res.data)
                pageIdx++
            }
            _attractions.postValue(_attractions.value)
            callback?.invoke()
        }
    }
}