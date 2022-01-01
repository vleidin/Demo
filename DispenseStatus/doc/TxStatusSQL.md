SELECT T.STORENUM, 
       T.TXNNUM,
       v.cddescr 
  FROM TX     T,
       TXSTAT X,
       txfillstattyp V 
 WHERE T.TXKEY = X.TXKEY 
   AND x.txfillstattypkey = v.txfillstattypkey 
-- AND T.STORENUM = ?   AND T.TXNNUM = ? 
 fetch first 15 rows only;

STORENUM TXNNUM		CDDESCR
2807	12668395	Complete
1414	12667237	Waiting for pickup
7589	12624494	Cancelled
2807	12624493	Deferred
2807	12624492	Complete
2804	10922615	Complete
2807	12624308	Complete
2807	12624191	Complete
2807	12655118	Complete
2807	12651016	Complete
2807	12654017	Complete
2807	12654016	Complete
2807	12647434	Complete
2807	12624178	Complete
2807	12624184	Complete