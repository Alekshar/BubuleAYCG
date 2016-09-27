package project.test;

import java.io.IOException;

import org.junit.Test;

import project.Loader;

public class LoaderTest {
	@Test
	public void test_convert_success() throws IOException{
		Loader.convert("data/norma_N5_tau4_dt2_delai820_000001.txt", "data/file0001.dgs");
	}
	
	@Test(expected=IOException.class)
	public void test_convert_badDestinationFormat() throws IOException{
		Loader.convert("data/norma_N5_tau4_dt2_delai820_000001.txt", "data/file0001.oups");
	}
}
