package com.raqun.dctracker.ui.home

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.raqun.dctracker.data.DataBean
import com.raqun.dctracker.data.source.remote.DiffRemoteDataSource
import com.raqun.dctracker.model.Diff
import com.raqun.dctracker.model.UiDataBean
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import com.raqun.dctracker.data.Error
import io.reactivex.rxkotlin.subscribeBy

/**
 * Created by tyln on 12/09/2017.
 */
class HomeViewModel @Inject constructor(private val diffRemoteDataSource: DiffRemoteDataSource)
    : ViewModel() {

    private val diffsLiveData = MutableLiveData<DataBean<List<Diff>>>()

    init {
        getDiffs()
    }

    fun getDiffsLiveData() = diffsLiveData

    private fun getDiffs() {
        diffsLiveData.value = UiDataBean.fetching(null)
        diffRemoteDataSource.getDiffs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { diffsLiveData.value = UiDataBean.success(it) },
                        onError = { diffsLiveData.value = UiDataBean.error(null, Error(0, it.localizedMessage)) })
    }
}