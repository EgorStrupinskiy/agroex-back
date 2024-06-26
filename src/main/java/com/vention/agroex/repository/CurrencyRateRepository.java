package com.vention.agroex.repository;

import com.vention.agroex.entity.CurrencyRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRateEntity, Long> {
    Optional<CurrencyRateEntity> findBySourceCurrencyAndTargetCurrency(String source, String target);

    @Query(value = "SELECT DISTINCT currency from currency_rates", nativeQuery = true)
    Set<String> findDistinctCurrencies();

    Set<CurrencyRateEntity> findUniqueBySourceCurrency(String source);

    Optional<List<CurrencyRateEntity>> findBySourceCurrency(String source);
}
