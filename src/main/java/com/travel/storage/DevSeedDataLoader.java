package com.travel.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.model.entity.Building;
import com.travel.model.entity.Comment;
import com.travel.model.entity.Diary;
import com.travel.model.entity.DiaryDestination;
import com.travel.model.entity.Facility;
import com.travel.model.entity.Food;
import com.travel.model.entity.Restaurant;
import com.travel.model.entity.Road;
import com.travel.model.entity.ScenicArea;
import com.travel.model.entity.ScenicAreaTag;
import com.travel.model.entity.Tag;
import com.travel.model.entity.User;
import com.travel.model.entity.UserInterest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 开发环境种子数据加载器。
 *
 * <p>用于数据库不可用时向内存仓库写入可测试数据，保障前端联调与检索演示。</p>
 */
@Component
public class DevSeedDataLoader
{

    private static final Logger log = LoggerFactory.getLogger(DevSeedDataLoader.class);

    private final InMemoryStore store;

    private final ObjectMapper objectMapper;

    private final ResourceLoader resourceLoader;

    private volatile boolean loaded;

    @Value("${app.dev-seed.enabled:false}")
    private boolean devSeedEnabled;

    @Value("${app.dev-seed.path:classpath:dev-seed}")
    private String devSeedPath;

    public DevSeedDataLoader(InMemoryStore store, ObjectMapper objectMapper, ResourceLoader resourceLoader)
    {
        this.store = store;
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
    }

    public synchronized void loadSeedIfEnabled(String reason)
    {
        if (!devSeedEnabled)
        {
            log.info("Dev seed data is disabled by config: app.dev-seed.enabled=false");
            return;
        }
        if (loaded)
        {
            log.info("Dev seed data already loaded, skip duplicate load.");
            return;
        }

        SeedBundle bundle = readSeedBundle();

        for (User u : bundle.users)
        {
            store.insertUser(u);
        }

        Map<Long, List<UserInterest>> interestsByUserId = new HashMap<>();
        for (UserInterest interest : bundle.userInterests)
        {
            interestsByUserId.computeIfAbsent(interest.getUserId(), k -> new java.util.ArrayList<>()).add(interest);
        }
        for (Map.Entry<Long, List<UserInterest>> entry : interestsByUserId.entrySet())
        {
            store.replaceUserInterests(entry.getKey(), entry.getValue());
        }

        for (ScenicArea s : bundle.scenicAreas)
        {
            store.insertScenicArea(s);
        }
        for (Tag t : bundle.tags)
        {
            store.insertTag(t);
        }
        for (ScenicAreaTag r : bundle.scenicAreaTags)
        {
            store.insertScenicAreaTag(r);
        }
        store.rebuildScenicAreaTagWeights();

        for (Building b : bundle.buildings)
        {
            store.insertBuilding(b);
        }
        for (Road r : bundle.roads)
        {
            store.insertRoad(r);
        }
        for (Facility f : bundle.facilities)
        {
            store.insertFacility(f);
        }
        for (Restaurant r : bundle.restaurants)
        {
            store.insertRestaurant(r);
        }
        for (Food f : bundle.foods)
        {
            store.insertFood(f);
        }
        for (Diary d : bundle.diaries)
        {
            store.insertDiary(d);
        }

        store.rebuildSearchIndicesAll();

        Map<Long, List<Long>> diaryDestinationIdsByDiaryId = new HashMap<>();
        for (DiaryDestination dd : bundle.diaryDestinations)
        {
            diaryDestinationIdsByDiaryId.computeIfAbsent(dd.getDiaryId(), k -> new java.util.ArrayList<>()).add(dd.getDestinationId());
        }
        for (Map.Entry<Long, List<Long>> entry : diaryDestinationIdsByDiaryId.entrySet())
        {
            store.replaceDiaryDestinations(entry.getKey(), entry.getValue());
        }

        for (Comment c : bundle.comments)
        {
            store.insertComment(c);
        }

        loaded = true;
        log.info("Dev seed data loaded from JSON path {} successfully (users={}, scenicAreas={}, facilities={}, foods={}, diaries={}, comments={}), reason={}",
                devSeedPath,
                bundle.users.size(),
                bundle.scenicAreas.size(),
                bundle.facilities.size(),
                bundle.foods.size(),
                bundle.diaries.size(),
                bundle.comments.size(),
                reason);
    }

    private SeedBundle readSeedBundle()
    {
        try
        {
            List<User> users = readList("users.json", new TypeReference<List<User>>()
            {
            });
            List<UserInterest> interests = readList("user_interests.json", new TypeReference<List<UserInterest>>()
            {
            });
            List<ScenicArea> scenicAreas = readList("scenic_areas.json", new TypeReference<List<ScenicArea>>()
            {
            });
            List<Tag> tags = readList("tags.json", new TypeReference<List<Tag>>()
            {
            });
            List<ScenicAreaTag> scenicAreaTags = readList("scenic_area_tags.json", new TypeReference<List<ScenicAreaTag>>()
            {
            });
            List<Building> buildings = readList("buildings.json", new TypeReference<List<Building>>()
            {
            });
            List<Road> roads = readList("roads.json", new TypeReference<List<Road>>()
            {
            });
            List<Facility> facilities = readList("facilities.json", new TypeReference<List<Facility>>()
            {
            });
            List<Restaurant> restaurants = readList("restaurants.json", new TypeReference<List<Restaurant>>()
            {
            });
            List<Food> foods = readList("foods.json", new TypeReference<List<Food>>()
            {
            });
            List<Diary> diaries = readList("diaries.json", new TypeReference<List<Diary>>()
            {
            });
            List<DiaryDestination> diaryDestinations = readList("diary_destinations.json", new TypeReference<List<DiaryDestination>>()
            {
            });
            List<Comment> comments = readList("comments.json", new TypeReference<List<Comment>>()
            {
            });

            return new SeedBundle(
                users,
                interests,
                scenicAreas,
                tags,
                scenicAreaTags,
                buildings,
                roads,
                facilities,
                restaurants,
                foods,
                diaries,
                diaryDestinations,
                comments
            );
        }
        catch (IOException ex)
        {
            throw new IllegalStateException("Failed to load dev seed json files from path: " + devSeedPath, ex);
        }
    }

    private <T> List<T> readList(String fileName, TypeReference<List<T>> typeReference) throws IOException
    {
        Resource resource = resourceLoader.getResource(resolveResourcePath(fileName));
        if (!resource.exists())
        {
            throw new IllegalStateException("Seed file not found: " + resolveResourcePath(fileName));
        }
        try (InputStream inputStream = resource.getInputStream())
        {
            return objectMapper.readValue(inputStream, typeReference);
        }
    }

    private String resolveResourcePath(String fileName)
    {
        if (devSeedPath.endsWith("/"))
        {
            return devSeedPath + fileName;
        }
        return devSeedPath + "/" + fileName;
    }

    private record SeedBundle(
        List<User> users,
        List<UserInterest> userInterests,
        List<ScenicArea> scenicAreas,
        List<Tag> tags,
        List<ScenicAreaTag> scenicAreaTags,
        List<Building> buildings,
        List<Road> roads,
        List<Facility> facilities,
        List<Restaurant> restaurants,
        List<Food> foods,
        List<Diary> diaries,
        List<DiaryDestination> diaryDestinations,
        List<Comment> comments
    )
    {
    }
}
