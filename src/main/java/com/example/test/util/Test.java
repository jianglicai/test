package com.example.test.util;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.wafv2.Wafv2Client;
import software.amazon.awssdk.services.wafv2.model.IPSetSummary;
import software.amazon.awssdk.services.wafv2.model.ListIpSetsRequest;
import software.amazon.awssdk.services.wafv2.model.ListIpSetsResponse;
import software.amazon.awssdk.services.wafv2.model.Scope;

import java.util.List;

public class Test {

    public static void main(String[] args) {
        List<Region> list = Region.regions();
        for (Region region : list) {
            System.out.println(region.id());
        }
    }
}
