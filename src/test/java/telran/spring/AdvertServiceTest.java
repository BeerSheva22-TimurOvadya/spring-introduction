package telran.spring;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import telran.spring.model.Advert;
import telran.spring.service.AdvertServiceImpl;

@SpringBootTest
class AdvertServiceTest {
	@Autowired
	AdvertServiceImpl advertService;

	@BeforeEach
	void clearAdverts() {
		advertService.clear();
	}

	private List<Advert> addSampleAdverts() {
		List<Advert> adverts = new ArrayList<>();
		adverts.add(advertService.addAdvert(createAdvert("Phone", "Electronics", 1000)));
		adverts.add(advertService.addAdvert(createAdvert("Laptop", "Electronics", 2000)));
		adverts.add(advertService.addAdvert(createAdvert("Shirt", "Clothing", 50)));
		return adverts;
	}

	private Advert createAdvert(String name, String category, int price) {
		Advert advert = new Advert();
		advert.setName(name);
		advert.setCategory(category);
		advert.setPrice(price);
		return advert;
	}

	@Test
	void testAddAdvert() {
		Advert advert = new Advert();
		advert.setCategory("Electronics");
		advert.setName("Phone");
		advert.setPrice(1000);
		Advert addedAdvert = advertService.addAdvert(advert);

		assertNotNull(addedAdvert);
		assertEquals(addedAdvert.getName(), advert.getName());
		assertEquals(addedAdvert.getCategory(), advert.getCategory());
		assertEquals(addedAdvert.getPrice(), advert.getPrice());
		assertNotNull(addedAdvert.getId());
	}

	@Test
	void testGetAllAdverts() {
		List<Advert> adverts = addSampleAdverts();
		List<Advert> retrievedAdverts = advertService.getAllAdverts();
		assertEquals(adverts.size(), retrievedAdverts.size());
	}

	@Test
	void testGetAdvertsByCategory() {
		addSampleAdverts();
		List<Advert> retrievedAdverts = advertService.getAdvertsByCategory("Electronics");
		assertEquals(2, retrievedAdverts.size());
	}

	@Test
	void testGetAdvertsByPrice() {
		addSampleAdverts();
		List<Advert> retrievedAdverts = advertService.getAdvertsByPrice(1500);
		assertEquals(2, retrievedAdverts.size());
	}

	@Test
	void testEditAdvert() {
		List<Advert> adverts = addSampleAdverts();
		Advert advert = adverts.get(0);
		advert.setName("New Name");
		Advert editedAdvert = advertService.editAdvert(advert.getId(), advert);
		assertEquals("New Name", editedAdvert.getName());
	}

	@Test
	void testDeleteAdvert() {
		List<Advert> adverts = addSampleAdverts();
		int advertId = adverts.get(0).getId();
		advertService.deleteAdvert(advertId);
		List<Advert> retrievedAdverts = advertService.getAllAdverts();
		assertEquals(2, retrievedAdverts.size());
	}

	@Test
	void testAddAdverts() {
		Advert advert1 = createAdvert("Phone", "Electronics", 1000);
		Advert advert2 = createAdvert("Laptop", "Electronics", 2000);
		List<Advert> adverts = Arrays.asList(advert1, advert2);
		List<Advert> addedAdverts = advertService.addAdverts(adverts);
		assertEquals(2, addedAdverts.size());
	}
}
