package com.devsuperior.dsmeta.services;

import com.devsuperior.dsmeta.dto.ReportDTO;
import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.dto.SummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class SaleService {

    @Autowired
    private SaleRepository repository;

    @Transactional(readOnly = true)
    public SaleMinDTO findById(Long id) {
        Optional<Sale> result = repository.findById(id);
        Sale entity = result.get();
        return new SaleMinDTO(entity);
    }

    @Transactional(readOnly = true)
    public Page<ReportDTO> getReport(String start, String stop, String name, Pageable pageable) {

        LocalDate maxDate;
        LocalDate minDate;

        maxDate = (stop == null) ? LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault()) : LocalDate.parse(stop);
        minDate = (start == null) ? maxDate.minusYears(1L) : LocalDate.parse(start);

        Page<Sale> sales = repository.searchBySellerNameAndDateRange(minDate, maxDate, name, pageable);
        Page<ReportDTO> reportDTO = sales.map(x -> new ReportDTO(x));
        return reportDTO;
    }

    @Transactional(readOnly = true)
    public Page<SummaryDTO> getSummary(String start, String stop, Pageable pageable) {

        LocalDate maxDate;
        LocalDate minDate;

        maxDate = (stop == null) ? LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault()) : LocalDate.parse(stop);
        minDate = (start == null) ? maxDate.minusYears(1L) : LocalDate.parse(start);

        Page<SummaryDTO> summaryDTO = repository.searchSalesPerSellerByDateRange(minDate, maxDate, pageable);
        return summaryDTO;
    }
}
