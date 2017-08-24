package io.redutan.nbasearc.monitoring.collector

import io.redutan.nbasearc.monitoring.collector.parser.LatencyParser
import io.redutan.nbasearc.monitoring.collector.parser.StatParser
import io.redutan.nbasearc.monitoring.persistence.LatencyRepository
import io.redutan.nbasearc.monitoring.persistence.LogRepository
import io.redutan.nbasearc.monitoring.persistence.StatRepository
import kotlin.reflect.KClass

/**
 *
 * @author myeongju.jung
 */
interface LogType<T : NbaseArcLog> {
    val parser: Parser<T>
    val command: String
    val logClass: KClass<T>
    val repository: LogRepository<T>
}

object LatencyType: LogType<Latency> {
    override val parser: Parser<Latency>
        get() = LatencyParser()
    override val command: String
        get() = "-l"
    override val logClass: KClass<Latency>
        get() = Latency::class
    override val repository: LogRepository<Latency>
        get() = LatencyRepository()
}

object StatType: LogType<Stat> {
    override val parser: Parser<Stat>
        get() = StatParser()
    override val command: String
        get() = "-s"
    override val logClass: KClass<Stat>
        get() = Stat::class
    override val repository: LogRepository<Stat>
        get() = StatRepository()

}
