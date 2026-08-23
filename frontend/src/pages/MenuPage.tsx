import { Link } from "react-router-dom";

const menuImages = {
  full: "/menu/full-menu.webp",
  combo1: "/menu/combo-1.webp",
  combo2: "/menu/combo-2.webp"
};

export default function MenuPage() {
  return (
    <main className="public-menu-page">
      <header className="public-menu-header">
        <div>
          <p className="public-menu-kicker">FACE MEDIA PRESENTS</p>
          <h1>মিরাবাই</h1>
          <p>হেইলা দুইলা, হেইলা দরবার নাচায়</p>
        </div>

        <div className="public-menu-actions">
          <a href="#full-menu">সম্পূর্ণ মেনু</a>
          <a href="#specials">কম্বো</a>
          <Link to="/">লগইন</Link>
        </div>
      </header>

      <section className="public-menu-intro">
        <span>আজকের মেনু</span>
        <h2>খাবার, নাস্তা, মিষ্টি ও পানীয়</h2>
        <p>
          নিচের মেনু থেকে খাবার ও দাম দেখুন। কম্বো অফারগুলো আলাদা বড় ছবিতে দেওয়া আছে।
        </p>
      </section>

      <section id="full-menu" className="public-menu-section">
        <div className="public-menu-section-heading">
          <div>
            <span>MENU</span>
            <h2>সম্পূর্ণ মেনু</h2>
          </div>
          <p>ছবিতে খাবারের নাম ও দাম দেখুন</p>
        </div>

        <a
          className="public-menu-poster-link"
          href={menuImages.full}
          target="_blank"
          rel="noreferrer"
          aria-label="সম্পূর্ণ মেনুর বড় ছবি খুলুন"
        >
          <img
            className="public-menu-poster"
            src={menuImages.full}
            alt="মিরাবাই সম্পূর্ণ খাবারের মেনু"
          />
        </a>
      </section>

      <section id="specials" className="public-menu-section public-menu-specials-section">
        <div className="public-menu-section-heading">
          <div>
            <span>SPECIAL</span>
            <h2>কম্বো অফার</h2>
          </div>
          <p>নির্দিষ্ট খাবারের বড় ছবি</p>
        </div>

        <div className="public-menu-special-grid">
          <article className="public-menu-special-card">
            <img
              src={menuImages.combo1}
              alt="কম্বো ১ - ঝাল মুড়ি ও চা"
            />
          </article>

          <article className="public-menu-special-card">
            <img
              src={menuImages.combo2}
              alt="কম্বো ২ - মিষ্টি, সিঙ্গারা ও পানি"
            />
          </article>
        </div>
      </section>

      <footer className="public-menu-footer">
        <strong>মিরাবাই</strong>
        <span>মেনু দেখতে এই পেজটি সবার জন্য উন্মুক্ত</span>
      </footer>
    </main>
  );
}
