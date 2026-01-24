@template.layout.main(
    title = "LifeVerdict - Financial Decision Platform",
    description = "The central hub for objective financial risk assessments. Access First Rent Verdict and future modules.",
    canonical = "https://movecostinfo.com/",
    content = @`
    <div style="background-color: var(--bg-body); min-height: 80vh; display: flex; flex-direction: column; justify-content: center; align-items: center; text-align: center; padding: 2rem;">
        
        <!-- Platform Brand -->
        <div style="margin-bottom: 3rem;">
            <div style="font-size: 0.8rem; letter-spacing: 0.15em; text-transform: uppercase; color: var(--text-tertiary); margin-bottom: 1rem;">
                Platform Services
            </div>
            <h1 style="font-size: 3.5rem; font-weight: 800; letter-spacing: -0.03em; margin: 0; background: linear-gradient(135deg, var(--text-primary) 0%, var(--text-secondary) 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent;">
                LifeVerdict
            </h1>
            <p style="font-size: 1.1rem; color: var(--text-secondary); max-width: 500px; margin: 1.5rem auto 0; line-height: 1.6;">
                Objective, data-driven frameworks for life's biggest financial decisions.
            </p>
        </div>

        <!-- Modules Grid -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 2rem; max-width: 1000px; width: 100%;">
            
            <!-- Module 1: First Rent Verdict (Active) -->
            <a href="/RentVerdict/" style="text-decoration: none; color: inherit; display: block;">
                <article style="background: var(--bg-surface); padding: 2.5rem; border-radius: 16px; border: 1px solid var(--border-subtle); transition: transform 0.2s cubic-bezier(0.16, 1, 0.3, 1), box-shadow 0.2s ease; height: 100%; text-align: left; position: relative; overflow: hidden;"
                         onmouseover="this.style.transform='translateY(-4px)'; this.style.boxShadow='0 12px 32px rgba(0,0,0,0.06)'"
                         onmouseout="this.style.transform='translateY(0)'; this.style.boxShadow='none'">
                    
                    <div style="position: absolute; top: 0; left: 0; width: 4px; height: 100%; background: var(--text-primary);"></div>
                    
                    <div style="margin-bottom: 1.5rem;">
                        <span style="background: rgba(45, 80, 22, 0.1); color: #2d5016; font-size: 0.7rem; font-weight: 700; padding: 0.4rem 0.8rem; border-radius: 100px; text-transform: uppercase; letter-spacing: 0.05em;">
                            Active Module
                        </span>
                    </div>

                    <h2 style="font-size: 1.5rem; font-weight: 700; margin-bottom: 0.5rem; color: var(--text-primary);">
                        First Rent Verdict
                    </h2>
                    <p style="font-size: 0.95rem; color: var(--text-secondary); line-height: 1.5; margin-bottom: 2rem;">
                        The 50/30/20 standard for rental affordability. Audit your move-in risk before signing the lease.
                    </p>

                    <div style="font-weight: 600; font-size: 0.9rem; color: var(--text-primary); display: flex; align-items: center;">
                        Launch Engine <span style="margin-left: 0.5rem;">&rarr;</span>
                    </div>
                </article>
            </a>

            <!-- Module 2: Comparison Pilot (Coming Soon) -->
             <article style="background: var(--bg-body); padding: 2.5rem; border-radius: 16px; border: 1px dashed var(--border-subtle); height: 100%; text-align: left; opacity: 0.7;">
                <div style="margin-bottom: 1.5rem;">
                    <span style="background: var(--bg-panel); color: var(--text-tertiary); font-size: 0.7rem; font-weight: 700; padding: 0.4rem 0.8rem; border-radius: 100px; text-transform: uppercase; letter-spacing: 0.05em;">
                        In Development
                    </span>
                </div>

                <h2 style="font-size: 1.5rem; font-weight: 700; margin-bottom: 0.5rem; color: var(--text-tertiary);">
                    Lease vs. Buy
                </h2>
                <p style="font-size: 0.95rem; color: var(--text-tertiary); line-height: 1.5; margin-bottom: 2rem;">
                    Advanced total cost of ownership modeling for vehicle acquisition decisions.
                </p>

                <div style="font-weight: 600; font-size: 0.9rem; color: var(--text-tertiary); cursor: not-allowed;">
                    Access Restricted
                </div>
            </article>

        </div>

    </div>
`)
