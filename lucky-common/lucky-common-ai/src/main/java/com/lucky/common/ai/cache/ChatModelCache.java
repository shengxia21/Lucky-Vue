package com.lucky.common.ai.cache;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * ChatModel 缓存
 *
 * <p>按 (providerName, url, apiKey) 缓存 ChatModel 实例，避免每次请求都重建
 * HTTP 客户端与连接池，提升流式聊天性能。</p>
 *
 * <p>缓存 key 使用 SHA-256 摘要，避免 apiKey 明文驻留内存。</p>
 *
 * @author lucky
 */
@Component
public class ChatModelCache {

    /**
     * 缓存条目存活时间：30 分钟
     */
    private static final long TTL_MS = 30 * 60 * 1000L;

    /**
     * 最大缓存条目数，超过时触发惰性清理
     */
    private static final int MAX_SIZE = 100;

    private final ConcurrentHashMap<String, CachedModel> cache = new ConcurrentHashMap<>();

    /**
     * 获取或创建 ChatModel
     *
     * @param providerName 服务提供商名称
     * @param url          基础 URL（允许为空）
     * @param apiKey       密钥
     * @param supplier     缓存未命中时的 ChatModel 构造逻辑
     * @return ChatModel 实例
     */
    public ChatModel getOrCreate(String providerName, String url, String apiKey, Supplier<ChatModel> supplier) {
        String key = buildKey(providerName, url, apiKey);
        long now = System.currentTimeMillis();
        // 惰性清理：遍历时移除过期项，控制缓存规模
        if (cache.size() > MAX_SIZE) {
            cache.entrySet().removeIf(entry -> entry.getValue().isExpired(now));
        }
        CachedModel cached = cache.get(key);
        if (cached != null && !cached.isExpired(now)) {
            return cached.model;
        }
        ChatModel model = supplier.get();
        cache.put(key, new CachedModel(model, now + TTL_MS));
        return model;
    }

    /**
     * 主动失效指定提供商 + url + apiKey 的缓存（用于 apiKey 轮换等场景）
     */
    public void invalidate(String providerName, String url, String apiKey) {
        cache.remove(buildKey(providerName, url, apiKey));
    }

    /**
     * 清空全部缓存
     */
    public void clear() {
        cache.clear();
    }

    /**
     * 构建缓存 key：providerName + SHA-256(url + apiKey)，避免 apiKey 明文做 key
     */
    private String buildKey(String providerName, String url, String apiKey) {
        String raw = (url == null ? "" : url) + "|" + (apiKey == null ? "" : apiKey);
        return providerName + ":" + sha256Hex(raw);
    }

    /**
     * 使用 JDK 原生 MessageDigest 计算 SHA-256 摘要（Hex 格式），避免引入额外依赖
     */
    private static String sha256Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 是 JDK 标准算法，理论上不会抛出；兜底用 hashCode
            return Integer.toHexString(input.hashCode());
        }
    }

    /**
     * 缓存条目
     */
    private static class CachedModel {

        final ChatModel model;
        final long expireAt;

        CachedModel(ChatModel model, long expireAt) {
            this.model = model;
            this.expireAt = expireAt;
        }

        boolean isExpired(long now) {
            return now >= expireAt;
        }

    }

}
