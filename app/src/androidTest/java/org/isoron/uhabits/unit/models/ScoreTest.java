/*
 * Copyright (C) 2016 Álinson Santos Xavier <isoron@gmail.com>
 *
 * This file is part of Loop Habit Tracker.
 *
 * Loop Habit Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Loop Habit Tracker is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.isoron.uhabits.unit.models;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.isoron.uhabits.BaseTest;
import org.isoron.uhabits.models.Checkmark;
import org.isoron.uhabits.models.Score;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class ScoreTest extends BaseTest
{
    @Before
    public void setup()
    {
        super.setup();
    }

    @Test
    public void test_compute_withDailyHabit()
    {
        int checkmark = Checkmark.UNCHECKED;
        assertThat(Score.compute(1, 0, checkmark), equalTo(0));
        assertThat(Score.compute(1, 5000000, checkmark), equalTo(4740387));
        assertThat(Score.compute(1, 10000000, checkmark), equalTo(9480775));
        assertThat(Score.compute(1, Score.MAX_VALUE, checkmark), equalTo(18259478));

        checkmark = Checkmark.CHECKED_IMPLICITLY;
        assertThat(Score.compute(1, 0, checkmark), equalTo(0));
        assertThat(Score.compute(1, 5000000, checkmark), equalTo(4740387));
        assertThat(Score.compute(1, 10000000, checkmark), equalTo(9480775));
        assertThat(Score.compute(1, Score.MAX_VALUE, checkmark), equalTo(18259478));

        checkmark = Checkmark.CHECKED_EXPLICITLY;
        assertThat(Score.compute(1, 0, checkmark), equalTo(1000000));
        assertThat(Score.compute(1, 5000000, checkmark), equalTo(5740387));
        assertThat(Score.compute(1, 10000000, checkmark), equalTo(10480775));
        assertThat(Score.compute(1, Score.MAX_VALUE, checkmark), equalTo(Score.MAX_VALUE));
    }

    @Test
    public void test_compute_withNonDailyHabit()
    {
        int checkmark = Checkmark.CHECKED_EXPLICITLY;
        assertThat(Score.compute(1/3.0, 0, checkmark), equalTo(1000000));
        assertThat(Score.compute(1/3.0, 5000000, checkmark), equalTo(5916180));
        assertThat(Score.compute(1/3.0, 10000000, checkmark), equalTo(10832360));
        assertThat(Score.compute(1/3.0, Score.MAX_VALUE, checkmark), equalTo(Score.MAX_VALUE));

        assertThat(Score.compute(1/7.0, 0, checkmark), equalTo(1000000));
        assertThat(Score.compute(1/7.0, 5000000, checkmark), equalTo(5964398));
        assertThat(Score.compute(1/7.0, 10000000, checkmark), equalTo(10928796));
        assertThat(Score.compute(1/7.0, Score.MAX_VALUE, checkmark), equalTo(Score.MAX_VALUE));
    }

    @Test
    public void test_getStarStatus()
    {
        Score s = new Score();

        s.score = Score.FULL_STAR_CUTOFF + 1;
        assertThat(s.getStarStatus(), equalTo(Score.FULL_STAR));

        s.score = Score.FULL_STAR_CUTOFF;
        assertThat(s.getStarStatus(), equalTo(Score.FULL_STAR));

        s.score = Score.FULL_STAR_CUTOFF - 1;
        assertThat(s.getStarStatus(), equalTo(Score.HALF_STAR));
        
        s.score = Score.HALF_STAR_CUTOFF + 1;
        assertThat(s.getStarStatus(), equalTo(Score.HALF_STAR));

        s.score = Score.HALF_STAR_CUTOFF;
        assertThat(s.getStarStatus(), equalTo(Score.HALF_STAR));
        
        s.score = Score.HALF_STAR_CUTOFF - 1;
        assertThat(s.getStarStatus(), equalTo(Score.EMPTY_STAR));

        s.score = 0;
        assertThat(s.getStarStatus(), equalTo(Score.EMPTY_STAR));
    }
}
