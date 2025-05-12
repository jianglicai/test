package com.example.test.util;

import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.wafv2.Wafv2Client;
import software.amazon.awssdk.services.wafv2.model.*;

import java.util.Arrays;

public class WafIpWhitelistExampleBack {
    public static void main(String[] args) {
        // 创建WAFv2客户端
        try (Wafv2Client wafv2Client = Wafv2Client.create()) {
            // 配置要添加到WAF规则组的IP地址
            String ipAddress = "61.130.182.202/32";  // 您的IP地址范围

            // 创建IPSet
            CreateIpSetRequest createIPSetRequest = CreateIpSetRequest.builder()
                    .name("MyWhitelistIPSet")
                    .scope(Scope.REGIONAL)  // 设置为REGIONAL或CLOUDFRONT
                    .addresses(Arrays.asList(ipAddress))
                    .ipAddressVersion(IPAddressVersion.IPV4)  // 设置为IPv4
                    .description("A whitelist of allowed IPs")
                    .build();

            // 发送创建IPSet请求
            CreateIpSetResponse createIPSetResponse = wafv2Client.createIPSet(createIPSetRequest);
            String ipSetArn = createIPSetResponse.summary().arn();  // 获取IPSet ARN
            System.out.println("Created IPSet with ARN: " + ipSetArn);
            //System.out.println("Created IPSet with ARN: " + createIpSetResponse.arn());

            // 创建WebACL请求
            CreateWebAclRequest createWebACLRequest = CreateWebAclRequest.builder()
                    .scope(Scope.REGIONAL)  // 在此指定scope
                    .name("MyWebACL")
                    .defaultAction(DefaultAction.builder().allow(AllowAction.builder().build()).build())
                    .rules(Arrays.asList(
                            Rule.builder()
                                    .name("IPWhitelistRule")
                                    .action(RuleAction.builder().build())
                                    .statement(Statement.builder()
                                            .ipSetReferenceStatement(IPSetReferenceStatement.builder()
                                                    .arn(ipSetArn)  // 使用获取的 ARN
                                                    .build())
                                            .build())
                                    .priority(1)
                                    .build()))
                    .build();

            // 发送创建WebACL请求
            CreateWebAclResponse createWebACLResponse = wafv2Client.createWebACL(createWebACLRequest);
            System.out.println("Created WebACL with ARN: " + createWebACLResponse.summary().arn());
            //System.out.println("Created WebACL with ARN: " + createWebACLResponse.arn());

        } catch (SdkException e) {
            System.err.println("AWS SDK exception: " + e.getMessage());
        }
    }

}

