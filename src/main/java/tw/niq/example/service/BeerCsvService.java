package tw.niq.example.service;

import java.io.File;
import java.util.List;

import tw.niq.example.model.BeerCsvRecord;

public interface BeerCsvService {
	
	List<BeerCsvRecord> convertCsv(File file);

}
