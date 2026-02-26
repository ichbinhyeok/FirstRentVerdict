@import firstrentverdict.service.seo.CityContentGenerator.QnA
@import java.util.List
@param List<QnA> questions

<section style="margin-bottom: 6rem;">
    <h2 style="font-size: 1.5rem; margin-bottom: 2.5rem; text-transform: uppercase; color: var(--text-tertiary); letter-spacing: 0.05em;">Moving FAQ</h2>
    @for(QnA qna : questions)
        <div style="margin-bottom: 3rem;">
            <h3 style="font-size: 1.35rem; font-weight: 700; margin-bottom: 0.75rem;">${qna.question()}</h3>
            <p style="font-size: 1.1rem; line-height: 1.6; color: var(--text-secondary);">${qna.answer()}</p>
        </div>
    @endfor
</section>

