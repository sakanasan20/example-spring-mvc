package tw.niq.example.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBeanBuilder;

import tw.niq.example.model.BeerCsvRecord;

@Service
public class BeerCsvServiceImpl implements BeerCsvService {

	@Override
	public List<BeerCsvRecord> convertCsv(File file) {
		
		try {
			List<BeerCsvRecord> beerCsvRecordList = new CsvToBeanBuilder<BeerCsvRecord>(new FileReader(file))
					.withType(BeerCsvRecord.class)
					.build()
					.parse();
			
			return beerCsvRecordList;
			
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		
	}

}
