package com.solution.prode.manager

import com.solution.prode.constants.CacheKeys.ALL_TEAMS_VALUE
import com.solution.prode.model.Team
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
class ProdeCacheManager {

    @Cacheable(value = [ALL_TEAMS_VALUE], key = "#cacheKey", cacheManager = "cacheManagerOneDay")
    fun saveAllTeams(cacheKey: String, teams: List<Team>): List<Team> {

        return teams
    }

    @CacheEvict(value = [ALL_TEAMS_VALUE], key = "#cacheKey")
    fun evictAllTeams(cacheKey: String) { }
}
