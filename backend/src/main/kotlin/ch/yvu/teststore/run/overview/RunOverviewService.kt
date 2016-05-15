package ch.yvu.teststore.run.overview

import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.run.RunRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class RunOverviewService @Autowired constructor(
        open val runRepository: RunRepository,
        open val resultRepository: ResultRepository) {

    fun getLastRunOverview(testSuiteId: UUID): Optional<RunOverview> {
        val run = runRepository.findAllByTestSuiteId(testSuiteId).firstOrNull()
        if (run == null) {
            return Optional.empty()
        } else {
            return Optional.of(RunOverview(run))
        }
    }
}