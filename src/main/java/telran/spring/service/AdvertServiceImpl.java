package telran.spring.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import telran.spring.model.Advert;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class AdvertServiceImpl implements AdvertService {
	private Map<Integer, Advert> advertsById = new HashMap<>();
	private Map<String, List<Advert>> advertsByCategory = new HashMap<>();
	private TreeMap<Double, List<Advert>> advertsByPrice = new TreeMap<>();

	private static final String FILE_NAME = "adverts.json";

	public Advert addAdvert(Advert advert) {
		int id = generateUniqueId();

		addAdvertToMaps(id, advert);
		log.info("Adding advert: " + advert);
		return advert;
	}

	private void addAdvertToMaps(int id, Advert advert) {
		advert.setId(id);
		advertsById.put(id, advert);
		advertsByCategory.computeIfAbsent(advert.getCategory(), k -> new ArrayList<>()).add(advert);
		advertsByPrice.computeIfAbsent(advert.getPrice(), k -> new ArrayList<>()).add(advert);
	}

	public List<Advert> getAllAdverts() {
		List<Advert> result = new ArrayList<>(advertsById.values());

		return result;
	}

	public List<Advert> getAdvertsByCategory(String category) {
		List<Advert> result = new ArrayList<>(advertsByCategory.getOrDefault(category, Collections.emptyList()));

		return result;
	}

	public List<Advert> getAdvertsByPrice(double maxPrice) {
		List<Advert> result = new ArrayList<>();
		advertsByPrice.headMap(maxPrice, true).values().forEach(result::addAll);
		return result;
	}

	private Integer generateUniqueId() {
		Integer id;
		do {
			id = ThreadLocalRandom.current().nextInt(100000, 1000000);
		} while (advertsById.containsKey(id));
		return id;
	}

	public Advert editAdvert(int id, Advert advert) {
		Advert editedAdvert = advertsById.get(id);
		removeAdvertFromMaps(editedAdvert);
		addAdvertToMaps(id, advert);
		return advert;
	}

	public void deleteAdvert(int id) {
		Advert advert = advertsById.remove(id);
		removeAdvertFromMaps(advert);
	}

	private void removeAdvertFromMaps(Advert advert) {
		advertsByCategory.get(advert.getCategory()).remove(advert);
		advertsByPrice.get(advert.getPrice()).remove(advert);
	}

	public List<Advert> addAdverts(List<Advert> adverts) {
		List<Advert> addedAdverts = new ArrayList<>();
		for (Advert advert : adverts) {
			addedAdverts.add(addAdvert(advert));
		}
		return addedAdverts;
	}

	public void clear() {
		advertsById.clear();
		advertsByCategory.clear();
		advertsByPrice.clear();
	}

	@PostConstruct
	public void init() {
		File file = new File(FILE_NAME);
		if (file.exists()) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				List<Advert> adverts = objectMapper.readValue(file,
						objectMapper.getTypeFactory().constructCollectionType(List.class, Advert.class));
				addAdverts(adverts);
				log.info("Adverts have been loaded successfully");
			} catch (IOException e) {
				log.error("Failed to load adverts: ", e);
			}
		}
	}

	@PreDestroy
	public void shutdown() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(new File(FILE_NAME), getAllAdverts());
			log.info("Adverts have been saved successfully");
		} catch (IOException e) {
			log.error("Failed to save adverts: ", e);
		}
	}

}
