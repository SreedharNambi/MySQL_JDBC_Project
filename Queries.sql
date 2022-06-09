select season,count(season) from matches group by season order by season asc;											
select winner,count(winner) as "no. of matches won" from matches where winner  like "_%_" group by winner order by winner asc;						
select bowling_team,sum(extra_runs) as "extra_runs_conceeded" from deliveries where deliveries.matchid in (select id from matches where season=2016) group by bowling_team order by extra_runs_conceeded asc;
select bowler,sum(total_runs)*6/count(bowler) as "economy" from deliveries where deliveries.matchid in (select id from matches where season=2015) group by bowler order by economy asc limit 1;
select winner,count(winner) from matches where winner="Sunrisers Hyderabad" and city="Hyderabad";


