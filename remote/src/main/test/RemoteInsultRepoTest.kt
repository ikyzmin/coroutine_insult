import com.jaka.remote.RemoteChannelInsultRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class RemoteInsultRepoTest  {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `make insult request success`() {
        val repo = RemoteChannelInsultRepository()
        runBlocking  {
            val insult = repo.insult()
            println(insult.insult)
        }
    }
}