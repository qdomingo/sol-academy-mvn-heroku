package com.example.solacademy.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public final class OracleWalletBootstrap {
	private static final Logger log = LoggerFactory.getLogger(OracleWalletBootstrap.class);

	// Keep in sync with src/main/resources/
	private static final String WALLET_RESOURCE_DIR = "Wallet_KBRYVUIPLUD4WZ9T";

	private OracleWalletBootstrap() {
	}

	/**
	 * Ensures Oracle Wallet exists on the filesystem (not inside the jar/war).
	 * If ORACLE_WALLET_DIR is already provided via env/system property, it does nothing.
	 */
	public static void ensureWalletExtractedToTempIfNeeded() {
		String configuredWalletDir = firstNonBlank(
				System.getProperty("ORACLE_WALLET_DIR"),
				System.getenv("ORACLE_WALLET_DIR"));
		if (configuredWalletDir != null) {
			log.info("Oracle wallet directory provided: {}", configuredWalletDir);
			return;
		}

		Path tempWalletDir = Paths.get(System.getProperty("java.io.tmpdir"), WALLET_RESOURCE_DIR);
		if (Files.isDirectory(tempWalletDir) && Files.exists(tempWalletDir.resolve("tnsnames.ora"))) {
			System.setProperty("ORACLE_WALLET_DIR", tempWalletDir.toString());
			log.info("Oracle wallet directory already present at {}", tempWalletDir);
			return;
		}

		try {
			extractWalletResources(tempWalletDir);
			System.setProperty("ORACLE_WALLET_DIR", tempWalletDir.toString());
			log.info("Oracle wallet extracted to {}", tempWalletDir);
		} catch (IOException ex) {
			throw new IllegalStateException(
					"Failed to extract Oracle wallet resources to " + tempWalletDir + ". " +
					"Set ORACLE_WALLET_DIR to a valid directory as a workaround.",
				ex);
		}
	}

	private static void extractWalletResources(Path targetDir) throws IOException {
		Files.createDirectories(targetDir);

		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(
				OracleWalletBootstrap.class.getClassLoader());
		Resource[] resources = resolver.getResources("classpath*:" + WALLET_RESOURCE_DIR + "/*");
		if (resources.length == 0) {
			throw new IOException("No wallet resources found on classpath at " + WALLET_RESOURCE_DIR);
		}

		for (Resource resource : resources) {
			String filename = resource.getFilename();
			if (filename == null || filename.isBlank() || !resource.isReadable()) {
				continue;
			}
			Path destination = targetDir.resolve(filename);
			try (InputStream in = resource.getInputStream()) {
				Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
			}
		}
	}

	private static String firstNonBlank(String... values) {
		if (values == null) {
			return null;
		}
		for (String v : values) {
			if (v != null && !v.isBlank()) {
				return v;
			}
		}
		return null;
	}
}
