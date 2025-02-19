package com.devsuperior.dsmeta.services;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.dto.SummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public Page<SummaryDTO> getSummary(String start, String stop, Pageable pageable){
		LocalDate maxDate;
		LocalDate minDate;
		if (stop == null){
			maxDate = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		} else {
			maxDate = LocalDate.parse(stop);
		}
		if (start == null){
			minDate = maxDate.minusYears(1L);
		} else {
			minDate = LocalDate.parse(start);
		}

		Page<Sale> sales = repository.searchSalesByDateRange(minDate, maxDate, pageable);
		Page<SummaryDTO> summaryDTO = sales.map(x -> new SummaryDTO(x));
		return summaryDTO;
	}
}
