DELETE FROM Planet;
INSERT INTO Planet(id, name, is_inhabited, climate, has_weapons, radius) VALUES (1, 'The Earth', 1,  'mild', 1, 6371);
INSERT INTO Planet(id, name, is_inhabited, climate, has_weapons, radius) VALUES (2, 'Uranus', 0,  'cold', 0, 25362);
INSERT INTO Planet(id, name, is_inhabited, climate, has_weapons, radius) VALUES (3, 'Venus', 0,  'extremely hot', 0, 6051.8);
INSERT INTO Planet(id, is_inhabited, climate, has_weapons, radius) VALUES (4, 0,  'cold', 0, 0.1);