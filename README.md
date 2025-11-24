# GoalGuard-MonteCarlo-Planner
Java Monte Carlo Micro-Investment Planner for financial goal simulation

GoalGuard — Monte Carlo Micro-Investment Planner (Java Project)
Overview

GoalGuard is a Java-based financial simulation tool that helps users understand whether small periodic investments can grow enough to reach a long-term financial goal. It uses Monte Carlo simulation, geometric Brownian motion, and portfolio rebalancing to model realistic investment outcomes.

This project is developed as part of the Programming in Java course at on VITYARTHI.

## Features:
- Multi-asset portfolio simulation (Stocks, Bonds, Cash, etc.)

- Dollar-cost averaging (DCA) with configurable contribution frequency

- Monte Carlo simulation with thousands of trials

- Periodic rebalancing (monthly/quarterly/yearly/none)

- Goal-probability calculation (chance of meeting financial target)

- Output CSV with final balances for graph plotting

- Pure Java — no external libraries required

## Tech Stack:
- Language: Java
- Storage: Text/CSV files
- Execution: Terminal / PowerShell

## How to Compile:

### Step 1 — Generate list of Java files
```shell
gci -Recurse "src" -Filter "*.java" | select -Expand FullName > sources.txt

```
### Step 2 — Compile
```shell
javac -d out @sources.txt

```
### How to Run:
```shell
java -cp out Main

```
### Input Files:
```sql
# name, weight, annualReturn%, vol%
US_Stocks,0.60,8.0,15.0
Intl_Stocks,0.20,7.0,18.0
Bonds,0.15,3.0,6.0
Cash,0.05,1.5,1.0

```
### sample_config.txt
```text
initial_balance=1000
contribution=100
contrib_frequency=12
target_goal=50000
years=10
steps_per_year=12
trials=5000
rebalance=monthly

```
## Output:
Program prints:

Success probability

Median final balance

Percentiles (25th, 75th)

Sample trial path

Saves: output/final_balances.csv

## Screenshots
see : /Screenshots

## Author
***Prakhar Shukla***
B.Tech CSE, VIT Bhopal