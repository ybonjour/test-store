package ch.yvu.teststore.run

import ch.yvu.teststore.common.TestStoreRepository
import org.springframework.stereotype.Repository

@Repository
interface RunRepository : TestStoreRepository<Run>