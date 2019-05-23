package com.hike.testapp.mvp.presenter

import com.hike.testapp.common.model.PhotoRepository
import com.hike.testapp.mvp.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PresenterImpl : Presenter {

    val photoRepository = PhotoRepository.instance
    var view: View? = null
    var query = "hello"
    var page = 1

    override fun loadPhotos(query: String) {
        this.query = query
        view?.showLoader()
        loadPhotos()
    }

    override fun loadNextPage() {
        this.page = page + 1
        loadPhotos()
    }

    override fun attachView(view: View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    fun loadPhotos() {
        photoRepository.loadPhotos(query, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    view?.hideLoader()
                    view?.appendToList(result.hits)
                },
                { it.printStackTrace() }
            )
    }
}