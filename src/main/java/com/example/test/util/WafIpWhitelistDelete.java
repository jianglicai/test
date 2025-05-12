package com.example.test.util;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.wafv2.Wafv2Client;
import software.amazon.awssdk.services.wafv2.model.*;

import java.util.ArrayList;
import java.util.List;

public class WafIpWhitelistDelete {
    public static void main(String[] args) {
        // 设置 WAF 客户端
        try (Wafv2Client wafv2Client = Wafv2Client.builder()
                .region(Region.US_EAST_1)  // 设置合适的 AWS 区域
                .build()) {

            // 设置要操作的 IPSet ID 和作用域（可以是 REGIONAL 或 CLOUDFRONT）
            String ipSetId = "9d9269c4-7180-466e-bfe4-daa45f75133b";  // 替换为您的 IPSet ID
            String scope = "REGIONAL";         // 或 "CLOUDFRONT"
            String name = "MyWhitelistIPSet";
            // 要删除的 IP 地址（例如，CIDR 格式）
            String ipToRemove = "192.168.1.1/32";  // 替换为要删除的 IP 地址

            // 获取 IPSet 的信息并获取 lockToken
            GetIpSetRequest getIPSetRequest = GetIpSetRequest.builder()
                    .id(ipSetId)
                    .name(name)
                    .scope(Scope.fromValue(scope))
                    .build();

            // 获取当前的 IPSet 信息和锁定令牌
            GetIpSetResponse getIPSetResponse = wafv2Client.getIPSet(getIPSetRequest);
            IPSet ipSet = getIPSetResponse.ipSet();
            String lockToken = getIPSetResponse.lockToken(); // 获取 lockToken
            List<String> currentAddresses = new ArrayList<>();
            currentAddresses.addAll(ipSet.addresses());
            currentAddresses.removeIf(item -> item.equals(ipToRemove)); // 删除符合条件的元素

            // 创建一个更新请求，删除指定的 IP 地址
            UpdateIpSetRequest updateIPSetRequest = UpdateIpSetRequest.builder()
                    .id(ipSetId)
                    .name(name)
                    .scope(Scope.fromValue(scope))
                    .lockToken(lockToken)  // 使用获取的 lockToken
                    .addresses(currentAddresses)  // 设置要删除的 IP 地址列表
                    .build();


            // 执行删除操作
            wafv2Client.updateIPSet(updateIPSetRequest);
            System.out.println("Successfully removed IP address: " + ipToRemove);

        } catch (Exception e) {
            System.err.println("Error deleting IP address from IPSet: " + e.getMessage());
        }
    }

}

