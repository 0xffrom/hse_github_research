//package hse_github_research.core
//
//import kotlinx.coroutines.*
//import org.junit.Assert
//import org.junit.Test
//
//class ClientMonitorTest {
//
//    @Test
//    fun timeoutTest() {
//        runBlocking {
//            val clientMonitor =
//                ClientMonitor<String>(
//                    listOf("Client1", "Client2"),
//                    timeoutPerRequest = 3000L,
//                    maxRequestsPerClient = 2
//                )
//
//            clientMonitor.getClientBlocked()
//            clientMonitor.getClientBlocked()
//            clientMonitor.getClientBlocked()
//            clientMonitor.getClientBlocked()
//
//            val startTime1 = System.currentTimeMillis()
//            clientMonitor.getClientBlocked()
//            Assert.assertTrue(System.currentTimeMillis() - startTime1 > 3000)
//
//            clientMonitor.getClientBlocked()
//            clientMonitor.getClientBlocked()
//            clientMonitor.getClientBlocked()
//
//            val startTime2 = System.currentTimeMillis()
//            clientMonitor.getClientBlocked()
//            Assert.assertTrue(System.currentTimeMillis() - startTime2 > 3000)
//        }
//    }
//}
