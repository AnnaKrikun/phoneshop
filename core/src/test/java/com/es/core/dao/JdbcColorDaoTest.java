package com.es.core.dao;

import com.es.core.model.phone.Color;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/context/test-config.xml")
public class JdbcColorDaoTest {
    private static final String ERROR_SIZES = "Error: expected / actual size = ";
    private static final String ERROR_COLORS = "Found colors don't match actual colors";

    Long[] ColorIds = {1000L, 1001L, 1002L, 1003L, 1004L, 1005L, 1006L, 1007L, 1008L, 1009L, 1010L, 1011L, 1012L, 1013L};
    String[] ColorCodes = {"Black", "White", "Yellow", "Blue", "Red", "Purple", "Gray", "Green", "Pink", "Gold",
            "Silver", "Orange", "Brown", "256"};

    private List<Color> colorList;

    @Autowired
    private ColorDao colorDao;

    @Before
    public void init() {
        colorList = new ArrayList<>();
        for (int i = 0; i < ColorIds.length; i++) {
            colorList.add(new Color(ColorIds[i], ColorCodes[i]));
        }
    }

    @Test
    @DirtiesContext
    public void getAllColorsCorrectlyWhenGetColors() {
        List<Color> colorDaoList = colorDao.getAllColors();

        Assert.isTrue(colorDaoList.size() == colorList.size(), ERROR_SIZES + colorList.size()
                + " / " + colorDaoList.size());
        Assert.isTrue(colorList.containsAll(colorDaoList), ERROR_COLORS);
    }
}
