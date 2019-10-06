package com.maryang.fastrxjava.util

import io.reactivex.Single

class TestObservable {

    fun getInt1(): Single<Int> {
        return Single.fromCallable {
            1
        }
    }


}