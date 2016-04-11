package ch.yvu.teststore.result

import ch.yvu.teststore.common.TestStoreRepository
import org.springframework.stereotype.Repository

@Repository
interface ResultRepository : TestStoreRepository<Result> {
}

