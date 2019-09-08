package com.maryang.fastrxjava.util

import com.maryang.fastrxjava.entity.GithubRepo
import com.maryang.fastrxjava.entity.Sample
import com.maryang.fastrxjava.entity.User
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


object Operators {

    fun letSmaple() {

        val sample: String = "sameple"

        // 명령형
        if (sample != null) {
            parameter(sample)
        }


        // let과 run 본인 자신을 접근냐 receiver를 통해서 넘기나 차이
        // let과 run 마지막에 대입되는 값을 return해줌
        /*
            sample.let {
                parameter(it)
                1
            }.run { //int형

            }
         */
        // run과 apply 차이는 반환값 차이

        // 함수형
        // 해당 변수를 넘긴다 let, it으로 전달
        sample.let {
            parameter(it)
            1
        }.run {

        }

        // 해당 변수를 receiver({})로 넘긴다
        sample.run {
            // length == sample.length, 동일
        }

        // 자기 자신을 반환, 값을 초기화 할 때 사용함
        sample.apply {

        }

        // 인자를 자기 자신 사용, 반환을 함
        sample.also {
            it.length
            1
        }.run {

        }

        with(sample) {
            length
            1
        }.run {

        }

    }

    fun parameter(param: String) {
        // do something
    }

    fun collectionSample() {
        val list = emptyList<GithubRepo>()
        list.forEach {
            createIsuue(it.name)
        }

        // map, flatmap, filter, find: foreach 기본으로 동작
        // 원소 하나하나를 특정값으로 변경해서 반환
        list.map {
            it.name
        }

        list.flatMap {
            emptyList<String>()
        }

        list.filter { it.private }
        // 조건에 맞는 변수를 가진 첫번째 객체를 반환
        list.find { it.private }

        // 하나씩 값을 전달하여 작업 후 다음 인덱스에게 그 값을 전달
        // reduce는 리스트 내 목록과 동일한 값을 return
        val numbers = listOf(1,2,3,4)
        numbers.reduce { acc, number ->
            acc + number
        }

        // fold는 리턴되야 하는 값이 리스트 내 목록과 동일하지 않아도 됨 (reduce랑 다른점)
        val numbers2 = listOf("hello", "hi", "by")
        numbers2.fold(0) { acc, number ->
            acc + number.length
        }

        // 2개의 list가 하나로 통일
        val listOfList = listOf(listOf(1), listOf(2))
        listOfList.flatten()

        list.all {
            // 목록에 있는 모든 값들이 아래의 원소를 만족하는 경우 return true, 그렇지 않으면 false
            it.private
        }.run {

        }

        list.any {
            // 목록에 있는 값들이 아래의 원소를 하나라도 만족하는 경우 return true, 그렇지 않으면 false
            it.private
        }.run {

        }

        // 값 중복되는 아이템을 삭제
        val distinctList = listOf(1,1,2,3,4)
        distinctList.distinct()
        distinctList.distinctBy {
            // 어떤 조건에 부합하는 애들만 가지고 옴, 그 외에 제외
            it == 1
        }

        distinctList.groupBy {
            // 아래의 조건 기준으로 목록을 분리함
            it == 1
        }.let {
            it[true] == listOf(1,1)
            it[false] == listOf(2,3,4)
        }


    }

    private fun createIsuue(name: String) {
        // do something
    }

    fun rxjavaOperators() {
        // map은 반환하는 값을 oserbale type으로 반환함
        Single.just(true)
            .map {
                1
            }.run {

            }

        Single.just(true)
            .flatMap {
                Single.just(1)
            }.run {

            }

        // 1초에 유예를 두고 실행 -> 버튼 더블 클릭 안되도록 방지 가능
        // debounce: 가장 마지막의 명령을 실행된 후 1초 이내에 들어 오는 요청은 무시함
        // 예를 들어 3번 실행
        // 비슷한걸로 throttle 가 있음
        // throttleFirst: 처음 실행되고 그 이후 실행 무시
        // throttleLast: 마지막만 실행되고 그 이후 실행 무시
        Observable.just(true)
            .debounce(1, TimeUnit.SECONDS)
            .doOnNext {
                // 1번만 실행됨
            }

        // concat은 선언되는 순서대로 합쳐짐
        Single.concat(
            Single.just(true),
            Single.just(true)
        )

        // merge는 실행되는 순서로 합쳐짐
        Single.merge(
            Single.just(true),
            Single.just(true)
        )

        // 여러 형태의 데이트를 합성해서 하나로 변환해준다
        Single.zip<Boolean, Int, String>(
            Single.just(true),
            Single.just(1),
            BiFunction { first, second ->
                "1"
            }
        )

        // combineLastest 자기 자신과 가장 최근에 실행된 값들과 합성을 함
        /*Single.just(true)
            .compose()*/
    }

    fun executeService() {
        Single.just(true)
            .subscribeOn(Schedulers.io()) // thread pool
            .subscribeOn(Schedulers.computation()) // thread pool, 계잔작업할 때 사용, 서버 or Spring
            .subscribeOn(Schedulers.trampoline())
            .subscribeOn(Schedulers.newThread())
            .subscribeOn(Schedulers.single())
            .subscribeOn(AndroidSchedulers.mainThread())

    }

    fun zipExample() {
        Single.zip<Boolean, Boolean, String>(
            Single.just(true),
            Single.just(true)
                .retry(3),
            BiFunction { t1, t2 ->
                ""
            }
        )
    }

    fun <T> applySchedulers(observable: Single<T>): Single<T> {
        return observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun <T> applySchedulers(): SingleTransformer<T, T> {
        return SingleTransformer {
            it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    val schedulersTransformer = SingleTransformer<Any, Any> {
        it.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun <T> applySchedulersRecycle(): SingleTransformer<T, T> {
        return schedulersTransformer as SingleTransformer<T, T>
    }

    // 아래는 실습 강좌 코드
    fun kotlinOperators() {
        listOf<User>().let {
            it.forEach {

            }
            1
        }
        with(listOf<User>()) {
            forEach {

            }
            1
        }
        listOf<User>().run {
            forEach {

            }
            1
        }
        Sample().apply {
            data = 3
        }
        Sample().also {
            it.data
        }
    }

    fun collectionsExmaple() {
        // 기존 방식
        val list = listOf<User>()
        val list2 = mutableListOf<String>()
        list.forEach {
            list2.add(it.name)
        }

        // map
        listOf<User>().map {
            listOf(it.name)
        }

        // flatmap
        listOf<User>().flatMap {
            listOf(it.name)
        }

        // flatmap
        listOf<GithubRepo>().filter {
            it.star
        }

        listOf<GithubRepo>().find {
            it.star
        }

        // first
        // number = 1
        // acc = 0
        // acc + number = 1

        // second
        // number = 2
        // acc = 1
        // acc + number = 3

        // third
        // number = 3
        // acc = 3
        // acc + number = 6
        listOf(1, 2, 3).reduce { acc, number ->
            acc + number
        }

        // first
        // initial = 10
        // number = 1
        // acc = sample
        // initial + acc + number = 11

        // second
        // number = 2
        // acc = 11
        // acc + number = 13

        // third
        // number = 3
        // acc = 13
        // acc + number = 16
        listOf(1, 2, 3).fold(10f) { acc, number ->
            acc + number
        }

        listOf(listOf(1, 2, 3)).flatten()

        listOf(1, 2, 3).all { it == 1 }
        listOf(1, 2, 3).any { it == 1 }

        listOf(1, 1, 2, 2, 3, 4, 5).distinct()

        listOf(1, 1, 2, 2, 3, 4, 5).groupBy {
            it == 1
        }.run {
            // map(true) = listOf(1,1)
            // map(false) = listOf(2,2,3,4,5)
        }
    }

    fun rxJavaExample() {
        Single.concat(
            Single.just(listOf(1, 1, 1)),
            Single.just(listOf(2, 2))
        ).subscribe {
            // 1,1,1,2,2
        }

        Single.merge(
            Single.just(listOf(1, 1, 1)),
            Single.just(listOf(2, 2))
        ).subscribe {
            // 1,2,2,1,1
        }

        Single.zip<Boolean, Int, String>(
            Single.just(true),
            Single.just(1),
            BiFunction { first, second ->
                ""
            }
        ).subscribe({

        }, {

        })
    }

}

fun <T> Single<T>.applySchedulersExtension(): Single<T> =
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
