package com.example.test.util;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.wafv2.Wafv2Client;
import software.amazon.awssdk.services.wafv2.model.*;

public class WafIpWhitelistList {
    public static void main(String[] args) {
        // 创建 WAF 客户端
        try (Wafv2Client wafv2Client = Wafv2Client.builder()
                .region(Region.US_EAST_1)  // 替换为适当的 AWS 区域
                .build()) {

            // 设置要获取 IPSet 的 ID 和作用域（可以是 REGIONAL 或 CLOUDFRONT）
            String ipSetId = "9d9269c4-7180-466e-bfe4-daa45f75133b";  // 替换为您的 IPSet ID
            String scope = "REGIONAL";         // 可以是 REGIONAL 或 CLOUDFRONT
            String name = "MyWhitelistIPSet";

            // 获取 IPSet 的信息
            GetIpSetRequest getIPSetRequest = GetIpSetRequest.builder()
                    .id(ipSetId)
                    .name(name)
                    .scope(Scope.fromValue(scope))  // 设置作用域
                    .build();

            // 调用 AWS SDK 获取 IPSet 信息
            GetIpSetResponse getIPSetResponse = wafv2Client.getIPSet(getIPSetRequest);

            // 输出 IPSet 中的 IP 地址
            System.out.println("IPSet contains the following IP addresses:");
            for (String ip : getIPSetResponse.ipSet().addresses()) {
                System.out.println(ip);
            }
        } catch (Exception e) {
            System.err.println("Error retrieving IP whitelist from IPSet: " + e.getMessage());
        }
    }

}

