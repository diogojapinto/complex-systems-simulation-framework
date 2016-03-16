import pandas as pd

data = pd.read_csv('../../src/main/resources/trip_1.csv')

print(data.describe())