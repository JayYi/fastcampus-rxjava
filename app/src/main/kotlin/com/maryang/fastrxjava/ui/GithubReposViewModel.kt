package com.maryang.fastrxjava.ui

import com.maryang.fastrxjava.data.repository.GithubRepository
import com.maryang.fastrxjava.entity.GithubRepo
import com.maryang.fastrxjava.entity.User
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.schedulers.Schedulers

class GithubReposViewModel {
    private val repository = GithubRepository()

    fun getGithubRepos() : Single<List<GithubRepo>> {
        // retrofit의 api 호출 메소드를 호출하면
        // single 반환되는데 이 single의 동자은 api 호출이다.
        // api 호출이 끝나면 구독하는 observer가 동작하도록
        // 이벤트를 등록한다
        // subsctibeOn()은 observable의 작업을 시작하는 쓰레드를 선택
        // 중복해서 적을 경우 가장 마지막에 적힌 스레드에서 시작
        // observeOn은 Observable이 data을 전달할 때, 사용할 스레드를 지정
        // 즉, subscribe()가 실핼 될 때 threaㅇ

        return repository.getGithubRepos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getUser(): Maybe<User> {
        // maybe 받으면, onComplete 구현이 필요함, null 처리용!
        // completable 은 return 할 값이 없으므로 onSuccess 없다.
        // 작업이 끝났을 때 onComplete 에서 할 작업만 등록하면 된다
        return repository.getUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


        /*repository.getUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableMaybeObserver<User>() {
                override fun onSuccess(t: User) {
                    // do something
                }

                override fun onComplete() {
                    // user 값이 null 인 경우
                }

                override fun onError(e: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            })*/
    }

    // single, observable 차이
    // 개념적으로 동일하나 single 경우 onComplete 호출되므로, dispose 필요없음

    /*fun changeName(name: String) {
        // completable 예시
        repository.changeUserName(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableCompletableObserver() {
                override fun onComplete() {
                    // ex) view update
                }

                override fun onError(e: Throwable) {

                }

            })

    }*/
}