package com.example.test.util;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.services.wafv2.Wafv2Client;
import software.amazon.awssdk.services.wafv2.model.*;
import software.amazon.awssdk.regions.Region;

public class ListIpSets {

    public static void main(String[] args) {

        // 创建 WAF 客户端
        try (Wafv2Client wafv2Client = Wafv2Client.builder()
                .region(Region.AP_SOUTHEAST_2)  // 替换为适当的 AWS 区域
                .build()) {

            // 设置获取 IPSets 的作用域（可以是 REGIONAL 或 CLOUDFRONT）
            String scope = "REGIONAL";  // 可以是 REGIONAL 或 CLOUDFRONT

            // 创建 ListIPSets 请求
            ListIpSetsRequest listIPSetsRequest = ListIpSetsRequest.builder()
                    .scope(Scope.fromValue(scope))  // 设置作用域
                    .build();

            // 调用 AWS SDK 获取 IPSet 列表
            ListIpSetsResponse listIPSetsResponse = wafv2Client.listIPSets(listIPSetsRequest);
            if (listIPSetsResponse.ipSets().size()<=0) {
                System.out.println("IPSet ID: 不存在");
            }
            // 输出 IPSet 列表
            System.out.println("IPSets in the scope " + scope + ":");
            for (IPSetSummary ipSetSummary : listIPSetsResponse.ipSets()) {
                System.out.println("IPSet ID: " + ipSetSummary.id() +
                        ", Name: " + ipSetSummary.name() +
                        ", Description: " + ipSetSummary.description());
            }

        } catch (Exception e) {
            System.err.println("Error retrieving IPSet list: " + e.getMessage());
        }
    }
}
