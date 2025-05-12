package com.example.test.util.test;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.wafv2.Wafv2Client;
import software.amazon.awssdk.services.wafv2.model.IPSetSummary;
import software.amazon.awssdk.services.wafv2.model.ListIpSetsRequest;
import software.amazon.awssdk.services.wafv2.model.ListIpSetsResponse;
import software.amazon.awssdk.services.wafv2.model.Scope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetListIpSets {

    public static Object getListIpSets() {
        // 创建 WAF 客户端
        try (Wafv2Client wafv2Client = Wafv2Client.builder()
                .region(Region.AP_SOUTHEAST_1)  // 替换为适当的 AWS 区域
                .build()) {

            // 设置获取 IPSets 的作用域（可以是 REGIONAL 或 CLOUDFRONT）
            String scope = "REGIONAL";  // 可以是 REGIONAL 或 CLOUDFRONT

            // 创建 ListIPSets 请求
            ListIpSetsRequest listIPSetsRequest = ListIpSetsRequest.builder()
                    .scope(Scope.fromValue(scope))  // 设置作用域
                    .build();

            // 调用 AWS SDK 获取 IPSet 列表
            ListIpSetsResponse listIPSetsResponse = wafv2Client.listIPSets(listIPSetsRequest);

            // 输出 IPSet 列表
            System.out.println("IPSets in the scope " + scope + ":");
            for (IPSetSummary ipSetSummary : listIPSetsResponse.ipSets()) {
                System.out.println("IPSet ID: " + ipSetSummary.id() +
                        ", Name: " + ipSetSummary.name() +
                        ", Description: " + ipSetSummary.description());
            }
            List<Map<String,Object>> list = new ArrayList<>();
            for (IPSetSummary ipSetSummary : listIPSetsResponse.ipSets()) {
                Map<String,Object> map = new HashMap<>();
                map.put("ipSetId",ipSetSummary.id());
                map.put("ipSetName",ipSetSummary.name());
                map.put("ipSetDescription",ipSetSummary.description());
                list.add(map);
            }
            return list;

        } catch (Exception e) {
            System.err.println("Error retrieving IPSet list: " + e.getMessage());
            return null;
        }
    }
}
