package com.example.test.util;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.wafv2.model.*;
import software.amazon.awssdk.services.wafv2.Wafv2Client;
import software.amazon.awssdk.core.exception.SdkException;

import java.util.Arrays;

public class WafIpWhitelistExample {
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


            // 创建 visibilityConfig 配置
            VisibilityConfig visibilityConfig = VisibilityConfig.builder()
                    .sampledRequestsEnabled(true)  // 启用请求采样
                    .cloudWatchMetricsEnabled(true)  // 启用 CloudWatch 指标
                    .metricName("MyWebACLMetric")  // CloudWatch 指标名称
                    .build();

            // 创建 FieldToMatch 配置（此时指定 uriPath ）
            FieldToMatch fieldToMatch = FieldToMatch.builder()
                    .uriPath(UriPath.builder().build())  // 设置检查的 URI 路径
                    .build();

            // 将搜索字符串转换为 SdkBytes
            SdkBytes searchStringBytes = SdkBytes.fromUtf8String("example");

            // 创建 ByteMatchStatement 配置
            ByteMatchStatement byteMatchStatement = ByteMatchStatement.builder()
                    .fieldToMatch(fieldToMatch)  // 设置 FieldToMatch
                    .searchString(searchStringBytes)  // 设置要匹配的字串
                    .positionalConstraint(PositionalConstraint.EXACTLY)  // 设置匹配类型
                    .textTransformations(TextTransformation.builder()
                            .type(TextTransformationType.NONE)  // 设置文本转换（不转换）
                            .build())
                    .build();

            // 创建规则（Rule），并为每个规则指定 visibilityConfig
            Rule rule = Rule.builder()
                    .name("AllowAllRule")
                    .priority(1)
                    .action(RuleAction.builder().allow(AllowAction.builder().build()).build())
                    .visibilityConfig(visibilityConfig)  // 为规则提供 visibilityConfig
                    .statement(Statement.builder()
                            //.byteMatchStatement(byteMatchStatement)  // 这里设置 ByteMatchStatement
                            .ipSetReferenceStatement(IPSetReferenceStatement.builder()
                                    .arn(ipSetArn)
                                    .build())
                            .build())
                    .build();
            // 创建WebACL请求
            CreateWebAclRequest createWebACLRequest = CreateWebAclRequest.builder()
                    .scope(Scope.REGIONAL)  // 在此指定scope
                    .name("MyWebACL")
                    .rules(Arrays.asList(rule))  // 将规则添加到 WebACL 中
                    .defaultAction(DefaultAction.builder().allow(AllowAction.builder().build()).build())
                    .visibilityConfig(visibilityConfig)  // 为 WebACL 提供 visibilityConfig
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

