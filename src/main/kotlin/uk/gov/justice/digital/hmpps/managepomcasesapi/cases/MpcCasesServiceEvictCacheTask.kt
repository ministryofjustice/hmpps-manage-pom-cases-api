package uk.gov.justice.digital.hmpps.managepomcasesapi.cases

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class MpcCasesServiceEvictCacheTask {
  companion object {
    val LOG: Logger = LoggerFactory.getLogger(this::class.java)
  }

  // every ten minutes
  @Scheduled(fixedDelay = 600000)
  @CacheEvict(value = ["MpcCasesService.forPrison"], allEntries = true)
  fun evictMpcCasesCache() {
    LOG.info("Evicting MPC Cases caches")
  }
}
