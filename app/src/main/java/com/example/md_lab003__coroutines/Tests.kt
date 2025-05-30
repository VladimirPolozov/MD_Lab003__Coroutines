package com.example.md_lab003__coroutines

import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.example.md_lab003__coroutines.model.network.RickAndMortyApiService
import com.example.md_lab003__coroutines.ui.screens.RickAndMortyUiState
import com.example.md_lab003__coroutines.ui.screens.RickAndMortyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.isActive
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalCoroutinesApi
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

@ExperimentalCoroutinesApi
class RickAndMortyViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: RickAndMortyApiService
    private lateinit var viewModel: RickAndMortyViewModel

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RickAndMortyApiService::class.java)

        viewModel = RickAndMortyViewModel(
            api = api,
            dispatcher = UnconfinedTestDispatcher()
        )
    }

    @Test
    fun rickAndMortyViewModelFetchCharactersReturnsTrue() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""{"info":{"count":1,"pages":1,"next":null,"prev":null},"results":[{"id":1,"name":"Rick Sanchez","status":"Alive","species":"Human","type":"","gender":"Male","origin":{"name":"Earth (C-137)","url":"https://rickandmortyapi.com/api/location/1"},"location":{"name":"Citadel of Ricks","url":"https://rickandmortyapi.com/api/location/3"},"image":"https://rickandmortyapi.com/api/character/avatar/1.jpeg","episode":["https://rickandmortyapi.com/api/episode/1"],"url":"https://rickandmortyapi.com/api/character/1","created":"2017-11-04T18:48:46.250Z"}]}""")
        mockWebServer.enqueue(mockResponse)

        viewModel.rickAndMortyUiState.test {
            viewModel.fetchCharacters()

            val loadingState = awaitItem()
            assert(loadingState == RickAndMortyUiState.Loading)

            val successState = awaitItem()
            assert(successState == RickAndMortyUiState.Success)
        }

        viewModel.characters.test {
            val characters = awaitItem()
            println(characters)
            assert(characters[0].id == 1)
        }
    }

    @Test
    fun rickAndMortyViewModelRickAndMortyUiStateErrorReturnsTrue() = runTest {
        val mockResponse = MockResponse().setResponseCode(404)
        mockWebServer.enqueue(mockResponse)

        viewModel.rickAndMortyUiState.test {
            viewModel.fetchCharacters()
            val loadingState = awaitItem()
            assert(loadingState == RickAndMortyUiState.Loading)
            val errorState = awaitItem()
            assert(errorState == RickAndMortyUiState.Error)
        }
    }

    @Test
    fun rickAndMortyViewModelRickAndMortyUiStateSuccessReturnsTrue() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""{"results":[{"id":1,"name":"Rick Sanchez","species":"Human","status":"Alive","image":"https://rickandmortyapi.com/api/character/avatar/1.jpeg"}]}""")
        mockWebServer.enqueue(mockResponse)

        viewModel.rickAndMortyUiState.test {
            viewModel.fetchCharacters()
            val loadingState = awaitItem()
            assert(loadingState == RickAndMortyUiState.Loading)
            val successState = awaitItem()
            assert(successState == RickAndMortyUiState.Success)
        }
    }

    @Test
    fun rickAndMortyViewModelViewModelScopeCancelReturnsTrue() = runTest {
        viewModel.fetchCharacters()
        viewModel.clearForTest()
        assert(!viewModel.viewModelScope.isActive)
    }

    @After
    fun shutdown() {
        mockWebServer.shutdown()
    }
}