package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.SummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query(value = "SELECT obj " +
            "FROM Sale obj " +
            "JOIN FETCH obj.seller " +
            "WHERE UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :name, '%')) " +
            "AND (obj.date BETWEEN :minDate and :maxDate)",
            countQuery = "SELECT COUNT(obj) " +
                    "FROM Sale obj " +
                    "JOIN obj.seller " +
                    "WHERE UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :name, '%')) " +
                    "AND (obj.date BETWEEN :minDate and :maxDate)")
    public Page<Sale> searchBySellerNameAndDateRange(LocalDate minDate, LocalDate maxDate, String name, Pageable pageable);

    @Query("SELECT new com.devsuperior.dsmeta.dto.SummaryDTO(obj.seller.name, SUM(obj.amount)) " +
            "FROM Sale obj " +
            "WHERE obj.date BETWEEN :minDate and :maxDate " +
            "GROUP BY obj.seller.name")
    public Page<SummaryDTO> searchSalesPerSellerByDateRange(LocalDate minDate, LocalDate maxDate, Pageable pageable);

}
