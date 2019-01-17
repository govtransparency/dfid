package eu.dl.core.cache;

import eu.dl.core.config.Config;
import redis.clients.jedis.Jedis;

/**
 *
 */
public final class RedisCache implements Cache {
    protected Jedis jedis;

    protected String prefix = "";

    /**
     * Redis cache initialisation.
     *
     * @param cachePrefix prefix for keys
     */
    public RedisCache(final String cachePrefix) {
        prefix = cachePrefix;

        Config config = Config.getInstance();
        String port= config.getParam("cache.redis.port");
        String host = config.getParam("cache.redis.host");

        jedis = new Jedis(host, Integer.valueOf(port));
    }

    @Override
    public void put(final String key, final String value) {
        jedis.set(prefix.concat(key), value);
    }

    @Override
    public String get(final String key) {
        return jedis.get(prefix.concat(key));
    }
}
