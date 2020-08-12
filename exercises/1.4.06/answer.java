a.
int sum = 0;                        // ~ 1
for (int n = N; n > 0; n /= 2)      // ~ 1
    for(int i = 0; i < n; i++)      // ~ log2(N)
        sum++;                      // ~ N

b.
int sum = 0;                        // ~ 1
for (int i = 1; i < N; i *= 2)      // ~ 1
    for (int j = 0; j < i; j++)     // ~ log2(N)
        sum++;                      // ~ N

c.
int sum = 0;                        // ~ 1
for (int i = 1 i < N; i *= 2)       // ~ 1
    for (int j = 0; j < N; j++)     // ~ log2(N)
        sum++;                      // ~ N * log2(N)