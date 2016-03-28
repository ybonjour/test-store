package ch.yvu.teststore.insert

import ch.yvu.teststore.insert.dto.TestSuiteDto
import ch.yvu.teststore.testsuite.TestSuite
import ch.yvu.teststore.testsuite.TestSuiteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID.randomUUID

@Service
class InsertService @Autowired constructor(val testSuiteRepository: TestSuiteRepository) {

    fun insertTestSuite(testSuiteDto: TestSuiteDto) {
        val testSuite = TestSuite(randomUUID(), testSuiteDto.name)
        testSuiteRepository.save(testSuite)
    }
}