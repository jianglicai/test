package com.example.test.util;

import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.wafv2.Wafv2Client;
import software.amazon.awssdk.services.wafv2.model.*;

import java.util.ArrayList;
import java.util.List;

public class WafIpWhitelistUpdate {
    public static void main(String[] args) {
        // 创建 WAF 客户端
        try (Wafv2Client wafv2Client = Wafv2Client.builder()
                .region(Region.US_EAST_1) // 根据您的区域选择正确的区域
                .build()) {

            // 要修改的 IPSet ID
            String ipSetId = "9d9269c4-7180-466e-bfe4-daa45f75133b"; // 替换为您的 IPSet ID
            String scope = "REGIONAL"; // 可以是 REGIONAL 或 CLOUDFRONT
            String name = "MyWhitelistIPSet";

            // 创建 IP 地址条件
            String ipAddressToAdd = "192.168.1.1/32"; // 要添加的 IP 地址（CIDR 格式）

            // 获取当前的 IPSet
            GetIpSetRequest getIPSetRequest = GetIpSetRequest.builder()
                    .id(ipSetId)
                    .name(name)
                    .scope(Scope.fromValue(scope))
                    .build();

            // 获取 IPSet 信息并获取锁定令牌
            GetIpSetResponse getIPSetResponse = wafv2Client.getIPSet(getIPSetRequest);
            IPSet ipSet = getIPSetResponse.ipSet();
            String lockToken = getIPSetResponse.lockToken(); // 获取 lockToken
            List<String> currentAddresses = new ArrayList<>();
            currentAddresses.addAll(ipSet.addresses());
            // 在现有的 IP 地址列表中添加新的 IP 地址
            currentAddresses.add(ipAddressToAdd);

            // 添加 IP 地址
            UpdateIpSetRequest updateIPSetRequestAdd = UpdateIpSetRequest.builder()
                    .id(ipSetId)
                    .name(name)
                    .scope(Scope.fromValue(scope))
                    .lockToken(lockToken)  // 使用获取到的 lockToken
                    .addresses(currentAddresses) // 添加的 IP 地址列表
                    .build();

            // 更新 IPSet，添加新的 IP 地址
            wafv2Client.updateIPSet(updateIPSetRequestAdd);
            System.out.println("Successfully updated IPSet with new IP address: " + ipAddressToAdd);

        } catch (SdkException e) {
            System.err.println("AWS SDK exception: " + e.getMessage());
        }
    }

}

