package com.mtt.myapp.common.util;


import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class UnitUtilTest {
	@Test
	public void testUnitUtil() {
		assertThat(UnitUtil.byteCountToDisplaySize(1024 * 1024 + 2)).isEqualTo("1.0MB");
		assertThat(UnitUtil.byteCountToDisplaySize(1024 * 1024 + (1024 * 1024 / 10))).isEqualTo("1.1MB");
		assertThat(UnitUtil.byteCountToDisplaySize(1024 * 1024 * 1024 + 2)).isEqualTo("1.0GB");
		assertThat(UnitUtil.byteCountToDisplaySize(1023)).isEqualTo("1023B");
		assertThat(UnitUtil.byteCountToDisplaySize(1024)).isEqualTo("1.0KB");
	}
}
