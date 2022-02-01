package com.example.hsl_rebase_mono.util;

import com.example.hsl_rebase_mono.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public final class GraphqlSchemaReaderUtil {

	public static String getSchemaFromFileName(final String filename) {
		try {
			return new String(
					GraphqlSchemaReaderUtil.class.getClassLoader().getResourceAsStream("graphql/" + filename + ".graphql").readAllBytes());
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException("Failed to call HSL API");
		}
	}
}