package com.maryang.fastrxjava

import com.maryang.fastrxjava.data.repository.GithubRepository
import com.maryang.fastrxjava.entity.GithubRepo
import com.maryang.fastrxjava.ui.repos.GithubReposPresenter
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class GithubReposPresenterTest {

    private lateinit var presenter: GithubReposPresenter
    @Mock
    lateinit var view: GithubReposPresenter.View
    @Mock
    lateinit var githubRepository: GithubRepository
    // stubbing, 테스트를 위한 임시 데이터를 생성
    private val repos: List<GithubRepo> = emptyList()
    private val searchText = "searchText"

    @Before // 모든 테스트마다 실행이
    fun setup() {
        MockitoAnnotations.initMocks(this)
        // 전역적으로 Rxjava를 통해서 사용되는 thread 바꿔준다. trampoline 현재 thread 유지
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        presenter = GithubReposPresenter(view, githubRepository)

        Mockito.`when`(githubRepository.searchGithubRepos(Mockito.anyString())) // test할 value 설정
            .thenReturn(Single.just(repos))
        Mockito.`when`(githubRepository.checkStar(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(Completable.complete())
    }

    @Test
    fun searchTest() {
        presenter.searchGithubRepos(searchText)
        Mockito.verify(view).showRepos(repos)
    }

    @Test
    fun searchSubjectTest() {
        val test = presenter.searchSubject()
            .test()
        // 바로 aseortOf 하지 않은 이유: subject는 바로 subscribe 하지 않기 떄문에 별도로 실행을 함

        presenter.searchGithubRepos(searchText)
        test.assertValue {
            it == repos
        }

        test.assertOf {
            Assert.assertTrue(it.isDisposed)
        }
    }
}
