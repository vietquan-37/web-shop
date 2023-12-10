package com.vietquan.security.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsResponse {
    private int placedOrderCount;
    private int deliveredOrderCount;
    private int shippedOrderCount;
    private int client;
    private double totalRevenue;
}
