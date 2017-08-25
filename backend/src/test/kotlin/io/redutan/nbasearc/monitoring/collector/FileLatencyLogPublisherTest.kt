package io.redutan.nbasearc.monitoring.collector

import io.reactivex.observers.TestObserver
import io.redutan.nbasearc.monitoring.collector.parser.LatencyParser
import io.redutan.nbasearc.monitoring.collector.parser.LogHeaderParser
import io.redutan.nbasearc.monitoring.logger
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * @author myeongju.jung
 */
//@Ignore
class FileLatencyLogPublisherTest {
    val parser = LatencyParser()
    val headerParser = LogHeaderParser()
    var logPublisher = FileLatencyLogPublisher(parser, headerParser)

    companion object {
        val log by logger()
    }

    @Test
    fun testObserve_Each() {
        // given
        val to = TestObserver.create<Latency>()
        // when
        logPublisher.observe().subscribe(to)
        // then
        to.assertComplete()
        to.assertValueCount(15 * 23 - 1)
        to.assertNever({ !isValidLatency(it) })
    }

    private fun isValidLatency(it: Latency): Boolean {
        return try {
            assertLatency(it)
            true
        } catch (t: AssertionError) {
            log.error("invalid " + it, t)
            false
        }
    }

    private fun assertLatency(latency: Latency) {
        if (latency.isError()) {
            assertLoggedAt(latency)
            assertNotNull(latency.errorDescription)
            return
        }
        assertFalse(latency.isUnknown())
        assertLoggedAt(latency)
        assertTrue(latency.under1ms >= 0)
        assertTrue(latency.under2ms >= 0)
        assertTrue(latency.under4ms >= 0)
        assertTrue(latency.under8ms >= 0)
        assertTrue(latency.under16ms >= 0)
        assertTrue(latency.under32ms >= 0)
        assertTrue(latency.under64ms >= 0)
        assertTrue(latency.under128ms >= 0)
        assertTrue(latency.under256ms >= 0)
        assertTrue(latency.under512ms >= 0)
        assertTrue(latency.under1024ms >= 0)
        assertTrue(latency.over1024ms >= 0)
    }
}
