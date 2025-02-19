package com.devsuperior.dsmeta.dto;

import com.devsuperior.dsmeta.entities.Sale;

public class SummaryDTO {

    private String sellerName;
    private Long total;

    public SummaryDTO(){

    }

    public SummaryDTO(String sellerName, Long total) {
        this.sellerName = sellerName;
        this.total = total;
    }

    public SummaryDTO(Sale sale){
        this.sellerName = sale.getSeller().getName();
        this.total = sale.getSeller().getSales().stream().count();
    }

    public String getSellerName() {
        return sellerName;
    }

    public Long getTotal() {
        return total;
    }
}
